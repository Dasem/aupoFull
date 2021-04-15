import React, {useEffect} from "react";
import {Row} from "reactstrap";

import BookCard from "../book-card/book-card-container";

const Catalog = (props) => {

    useEffect(() => {
        props.getBooks();
    }, []);

    return (
        <div>
            <Row xs={"3"}>
                {
                    props.books && props.books.map(book => {
                        return (
                            <BookCard key={`book-${book.id}`} book={book} />
                        )
                    })
                }
            </Row>
        </div>
    );
}

export default Catalog;