import {SetBooksAction} from "../actions/books";

/**
 * initial state of the book list
 * @type {{books: []}}
 */
const initialState = {
    books: []
}

/**
 * The reducer function
 * @param state
 * @param action
 */
export default function (state = initialState, action) {
    switch (action.type) {
        case SetBooksAction: return {
            ...state,
            books: action.payload
        }
        default:
            return state;
    }
}


