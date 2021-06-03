import "./LandingPage.css";
import Box from "@material-ui/core/Box";
import { Row, Col, Container, Navbar } from "react-bootstrap";
import headerImage from "../../images/header-image.jpg";
import { Link } from "react-router-dom";
import React from "react";
import { useHistory } from "react-router-dom";
import Button from "@material-ui/core/Button";
import {Header} from "./Header";

function AccountButton() {
  const history = useHistory();
  function handleClick() {
    history.push("/account");
  }
  return (
    <button type="button" onClick={handleClick}>
      Account
    </button>
  );
}

function WishlistButton() {
  const history = useHistory();
  function handleClick() {
    history.push("/wishlist");
  }
  return (
      <button type="button" onClick={handleClick}>
        Wishlist
      </button>
  );
}

export const LandingPage = () => {
  return (
    <Container className="landingPage">
        <Header />
      <Row className="shopContent">
        Content
      </Row>
    </Container>
  );
};
