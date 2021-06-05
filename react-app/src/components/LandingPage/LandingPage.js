import "./LandingPage.css";
import { Row, Col, Container } from "react-bootstrap";
import React from "react";
import { Header } from "./Header";
import { ShopContent } from "./ShopContent";

export const LandingPage = () => {
  return (
    <Container className="landingPage">
      <Header />
      <ShopContent />
    </Container>
  );
};
