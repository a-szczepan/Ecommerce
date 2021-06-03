import "../components/styles/Product.css";
import { Row, Col, Container } from "react-bootstrap";
import { useState, useEffect } from "react";
import axios from "axios";
import Button from "@material-ui/core/Button";
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';


const useStyles = makeStyles({
  root: {
    maxWidth: 345,
  },
});

function ImgMediaCard(props) {
  const classes = useStyles();
  return (
      <Card className={classes.root}>
        <CardActionArea>
          <CardMedia
              component="img"
              alt="product"


              image={props.product.image}
              title={props.product.name}
          />
          <CardContent>
            <Typography gutterBottom variant="h5" component="h2">
              {props.product.name}
            </Typography>

          </CardContent>
        </CardActionArea>
        <CardActions>
          <Button size="small" color="primary">
            Share
          </Button>
          <Button size="small" color="primary">
            Learn More
          </Button>
        </CardActions>
      </Card>
  );
}

export const Products = () => {
  const url = `http://localhost:9000/products`;
  const [products, setProducts] = useState(null);

  useEffect(() => {
    axios.get(url).then((response) => {
      setProducts(response.data);
    });
  }, [url]);

    return (
        <>
            { products && products.map((product) => (
                <Col className="product">
                  <ImgMediaCard product={product} />
                </Col>
            ))}
        </>
    );
};
