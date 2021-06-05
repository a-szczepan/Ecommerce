import { Row, Col, Container } from "react-bootstrap";
import Products from "../Products";
import { SideBar } from "./SideBar";

export const ShopContent = () => {
  return (
    <Row className="shopContent">
      <Col className="sideBar">
        <SideBar />
      </Col>
      <Col className="products">
        <Products />
      </Col>
    </Row>
  );
};
