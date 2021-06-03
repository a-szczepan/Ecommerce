import {useHistory} from "react-router-dom";
import React from "react";
import {Col, Row} from "react-bootstrap";
import Button from "@material-ui/core/Button";

function AccountButton() {
    const history = useHistory();
    function handleClick() {
        history.push("/account");
    }
    return (
        <Button type="button" onClick={handleClick}>
            Konto
        </Button>
    );
}

function WishlistButton() {
    const history = useHistory();
    function handleClick() {
        history.push("/wishlist");
    }
    return (
        <Button type="button" onClick={handleClick}>
            Wishlist
        </Button>
    );
}

export const Navbar = () => {
    return(
        <Col className="navbar">
            <Row>
                <WishlistButton />
                <Button>Koszyk</Button>
                <AccountButton />
            </Row>
        </Col>
    );
}