import { Row, Col, Container } from "react-bootstrap";
import {Products} from "../Products";

export const ShopContent = () => {
    return (
        <Row className="shopContent">
            <Col className="sideBar">
                side bar
            </Col>
            <Col className="products">
                <Products />
            </Col>
        </Row>
    );
}