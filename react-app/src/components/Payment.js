import "../components/styles/Payment.css";
import { Row, Col, Container } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";

export const Payment = () => {
  const payment_id = 1;
  const url = `http://localhost:9000/payments/${payment_id}`;
  const [payment, setPayment] = useState(null);
  const [user, setUser] = useState(null);
  let content = null;
  useEffect(() => {
    axios.get(url).then((response) => {
      console.log(response.data);
      setPayment(response.data);
    });
  }, [url]);

  if (payment) {
    content = (
      <Row>
        <Box>Data zamówienia: </Box>
        <Box fontSize={12}>{payment.date}</Box>
        <Box>Kwota: </Box>
        <Box fontSize={12}>{payment.amount}</Box>
      </Row>
    );
  }

  return <Container className="payment">{content}</Container>;
};
