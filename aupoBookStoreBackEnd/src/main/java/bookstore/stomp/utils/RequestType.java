package bookstore.stomp.utils;

public enum RequestType {

    //books
    GET_BOOKS,
    CREATE_BOOK,
    DELETE_BOOK,

    // genres
    GET_GENRES,

    // order
    CREATE_ORDER,
    SEND_USER_ORDER,
    GET_ORDERS,
    DELETE_ORDER,

    // user
    GET_USERS,
    CREATE_USER,
    DELETE_USER,

    // authorization
    SIGN_IN,
    SIGN_UP
}
