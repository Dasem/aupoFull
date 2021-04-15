import {CreateBookAction, DeleteBookAction, GetBooksAction, SetBooks} from "../actions/books";
import * as Stomp from '@stomp/stompjs';
import * as Socket from 'sockjs-client';
import {ConnectStompAction} from "../actions/stomp";
import {GetGenresAction, SetGenres} from "../actions/genres";
import {auth, extractUser} from "../../components/consts/auth";
import {SignUpAction, SignInAction} from "../actions/authorisation";
import {
    DeleteOrderAction,
    GetOrdersAction,
    CreateOrderAction,
    SetOrders,
    SendUserOrderAction
} from "../actions/orders";
import {orderLocker} from "../../components/admin/orders";
import {CreateUserAction, DeleteUserAction, GetUsersAction, SetUsers} from "../actions/user";
import {bookLocker} from "../../components/admin/goods";
import {userLocker} from "../../components/admin/users";

/**
 * Middleware function
 */

let wsServerUrl = 'http://localhost:8080/bs';

let socket = new Socket(wsServerUrl);
export let client;

let tryToAction;

export default function stompMiddleware() {

    return store => next => action => {

        const stompFailure = error => {
            console.log(`Stomp failure: ${error}`);
            setTimeout(stompConnect, 3000);
            alert(`Ошибка: ${error}`);
        }

        const stompOnClose = () => {
            setTimeout(() => {
                socket = new Socket(wsServerUrl);
                stompConnect();
            }, 3000);
        }

        const stompConnect = () => {
            client = Stomp.Stomp.over(socket);
            let user = extractUser();
            client.connect({name: user?.login}, frame => { // создаём сокет сразу с зареганным пользователем
                    client.subscribe(action.payload, (message) => {
                        let data = JSON.parse(message.body);
                        // todo: если статус код 3xx - 4xx : страничку с ошибкой
                        if (data.status < 200 || data.status >= 300) {
                            alert(`Ошибка: ${data.customError}`);
                            return;
                        }
                        switch (data.requestType) {
                            case "GET_BOOKS":
                                store.dispatch(new SetBooks(data.content));
                                break;
                            case "CREATE_BOOK":
                                bookLocker.receiveOperation(data.content);
                                break;
                            case "DELETE_BOOK":
                                bookLocker.receiveOperation(data.content);
                                break;

                            case "GET_GENRES":
                                store.dispatch(new SetGenres(data.content));
                                break;

                            case "GET_ORDERS":
                                store.dispatch(new SetOrders(data.content));
                                break;

                            case "GET_USERS":
                                store.dispatch(new SetUsers(data.content));
                                break;
                            case "CREATE_USER":
                                userLocker.receiveOperation(data.content);
                                break;
                            case "DELETE_USER":
                                userLocker.receiveOperation(data.content);
                                break;
                            case "CREATE_ORDER":
                                orderLocker.receiveOperation(data.content);
                                break;
                            case "DELETE_ORDER":
                                orderLocker.receiveOperation(data.content);
                                break;
                            case "SEND_USER_ORDER":
                                alert('Покупка совершена');
                                break;

                            case "SIGN_IN":
                                // todo: обработать ситуацию с некорректной авторизацией
                                localStorage.setItem("user", JSON.stringify(data.content));
                                //store.dispatch(new ConnectStomp(`/user/bookstore`));
                                alert("Авторизация прошла успешно!");
                                window.location.reload(); // костыль, мне влом думать как в хедере перерисовывтаь кнопки
                                // todo: рабочий редирект на стартовую страницу (q) history.push(AUTH_COMPLETED_URL);
                                break;
                            case "SIGN_UP":
                                // todo: обработать ситуацию с некорректной регистрацией
                                // todo: автоматический вход после успешной регистрации
                                alert("Регистрация прошла успешно!");
                                break;
                            default:
                                console.log(`Unknown requestType: ${data.requestType}`);
                        }
                    })
                },
                error => stompFailure(error),
                () => stompOnClose()
            );
        }

        // todo: перенесены на WS не все действия, скорее всего и не будут все, потому что мне надоело
        const doAction = () => {
            switch (action.type) {
                case ConnectStompAction:
                    stompConnect();
                    break;

                case GetBooksAction:
                    client.send(`/books`, auth());
                    break;
                case CreateBookAction:
                    client.send(`/admin/books`, auth(), JSON.stringify(action.payload));
                    break;
                case DeleteBookAction:
                    client.send(`/admin/books/${action.payload}`, auth());
                    break;

                case GetOrdersAction:
                    client.send(`/admin/orders`, auth());
                    break;
                case CreateOrderAction:
                    client.send(`/admin/order`, auth(), JSON.stringify(action.payload));
                    break;
                case DeleteOrderAction:
                    client.send(`/admin/order/${action.payload}`, auth());
                    break;
                case SendUserOrderAction:
                    client.send(`/order`, auth(), JSON.stringify(action.payload));
                    break;

                case GetUsersAction:
                    client.send(`/admin/get-users`, auth());
                    break;
                case CreateUserAction:
                    client.send(`/admin/users`, auth(), JSON.stringify(action.payload));
                    break;
                case DeleteUserAction:
                    client.send(`/admin/users/${action.payload}`, auth());
                    break;

                case GetGenresAction:
                    client.send(`/genres`, auth());
                    break;

                case SignInAction:
                    client.send(`/signin`, {}, JSON.stringify(action.payload));
                    break;
                case SignUpAction:
                    client.send(`/signup`, {}, JSON.stringify(action.payload));
                    break;
                default:
                // do nothing
            }
            next({
                type: action.type,
                payload: action.payload
            })
        }

        // todo: просто временное решение на ещё одну попытку коннекта, в идеале это сделать луше, но мне влом
        // лечит падение при обновлении страницы с данными: сокет ещё не успел законнектиться, но его юзаем
        const cantAction = () => action.type !== ConnectStompAction && !client.connected;

        if (cantAction()) {
            clearInterval(tryToAction); // вдруг уже висел
            tryToAction = setInterval(() => {
                if (!cantAction()) {
                    doAction();
                    clearInterval(tryToAction);
                }
            }, 500)
        } else {
            doAction();
        }
    }
}