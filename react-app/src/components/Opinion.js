import "../components/styles/Opinion.css";
import { Row, Col, Container } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";

export const Opinion = () => {
  const opinion_id = 1;
  const url = `http://localhost:9000/opinions/${opinion_id}`;
  const [opinion, setOpinion] = useState(null);
  const [user, setUser] = useState(null);
  let content = null;
  useEffect(() => {
    axios.get(url).then((response) => {
      console.log(response.data);
      setOpinion(response.data);
    });
  }, [url]);

  if (opinion) {
    content = (
      <Row>
        <Box fontSize={12}>{opinion.opinion_txt}</Box>
      </Row>
    );
  }

  return <Container className="opinion">{content}</Container>;
};
