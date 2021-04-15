/**
 * initial state of the book list
 * @type {{orders: []}}
 */
import {SetOrdersAction} from "../actions/orders";

const initialState = {
    orders: []
}

/**
 * The reducer function
 * @param state
 * @param action
 */
export default function (state = initialState, action) {
    switch (action.type) {
        case SetOrdersAction: return {
            ...state,
            orders: action.payload
        }
        default:
            return state;
    }
}


