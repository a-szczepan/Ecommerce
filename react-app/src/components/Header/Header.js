import "./Header.css";
import Box from "@material-ui/core/Box";
import { Row, Col, Container } from "react-bootstrap";
import headerImage from "../../images/header-image.jpg";

export const Header = () => {
  return (
    <Container className="landingPage">
      <Col>
        <Box fontSize="h2.fontSize">The Plant Corner</Box>
      </Col>
      <Col>
        <img width="300px" src={headerImage} />
      </Col>
      <Col>
        <Box fontSize="h6.fontSize">
          Oferujemy tropikalne rośliny z całego świata. Wybieramy je dla Ciebie
          osobiście. Dostarczamy prosto pod Twoje drzwi.{" "}
        </Box>
      </Col>
    </Container>
  );
};
