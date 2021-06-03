import {Col, Row} from "react-bootstrap";
import Box from "@material-ui/core/Box";
import headerImage from "../../images/header-image.jpg";
import React from "react";
import {Navbar} from "./Navbar";
import {Typography} from "@material-ui/core";
import logo from "../../images/logo.png"


export  const Header = () => {
    return(
        <Row className="header">
            <Col className="headerTitle">
                <Row className="logo">
                    <img src={logo} height={70} width={70}/>
                </Row>
                <Row className="headerText">
                    <Box fontSize={48} >The Plant Corner</Box>
                    <Box fontSize={14} fontWeight={300} lineHeight={1.4} display="inline">
                        Oferujemy tropikalne rośliny z całego świata.<br />
                        Wybieramy je dla Ciebie osobiście.<br />
                        Dostarczamy prosto pod Twoje drzwi. <br />
                    </Box>
                </Row>
            </Col>
            <Col className="headerImage">
                <img  height="600px" src={headerImage} alt="header"  />
            </Col>
            <Navbar />
        </Row>
    );
}