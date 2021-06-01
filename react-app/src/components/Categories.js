import "../components/styles/Categories.css";
import { Row, Col, Container } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import axios from "axios";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";

export const Categories = () => {
  const url = `http://localhost:9000/categories`;
  const [categories, setCategories] = useState(null);
  let content = null;
  useEffect(() => {
    axios.get(url).then((response) => {
      console.log(response.data);
      setCategories(response.data);
    });
  }, [url]);

  return (
    <>
      <h1 className="categoryHeading">Kategorie produktów</h1>
      {categories.map((category) => (
        <>
          <Row className="shippingData">
            <Button color="primary">{category.name}</Button>
          </Row>
        </>
      ))}
    </>
  );
};
