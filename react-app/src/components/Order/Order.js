import "./Order.css";
import { Row, Container } from "react-bootstrap";
import React, { useEffect } from "react";
import Box from "@material-ui/core/Box";
import Paper from "@material-ui/core/Paper";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import {
  deleteFromCart,
  fetchCart,
  fetchProducts,
  getAccountInfo,
  getShippingInfo,
  quantityDown,
  quantityUp,
  setUser,
} from "../../redux/Shopping/shopping-actions";
import AddCircleOutlineIcon from "@material-ui/icons/AddCircleOutline";
import RemoveCircleOutlineIcon from "@material-ui/icons/RemoveCircleOutline";
import DeleteIcon from "@material-ui/icons/Delete";
import { useDispatch, useSelector } from "react-redux";
import { Divider } from "@material-ui/core";
import { AccountInfo } from "./AccountInfo";
import { ShippingInfo } from "./ShippingInfo";

const AcceptButton = (props) => {
  if (props.account !== "create" && props.shipping !== "create") {
    return (
      <Button variant="contained" color="primary">
        Przejdź dalej
      </Button>
    );
  } else {
    return <></>;
  }
};

export const Order = () => {
  const post = useSelector((state) => state);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(setUser());
    dispatch(fetchProducts());
    dispatch(fetchCart(post.shop.user));
    dispatch(getAccountInfo(post.shop.user));
    dispatch(getShippingInfo(post.shop.user));
  }, []);

  return (
    <Container className="order">
      <Typography
        variant="h5"
        style={{
          display: "flex",
          justifyContent: "center",
          paddingTop: "10px",
        }}
      >
        Koszyk ({post.shop.cart.length} art.){" "}
      </Typography>
      <Paper
        style={{
          minWidth: "30vw",
          overflow: "auto",
          display: "flex",
          justifyContent: "center",
          backgroundColor: "inherit",
        }}
        elevation={0}
      >
        <List>
          {post.shop.cart.map((x, index) => (
            <ListItem key={index} className="cartListItem">
              <Row>
                <img height="200px" src={x.image} />
              </Row>
              <Row>
                <Typography variant="h6" style={{ paddingLeft: "50px" }}>
                  {x.name}
                </Typography>
                <Box style={{ paddingLeft: "50px" }}>
                  {new Intl.NumberFormat("pl-PL", {
                    style: "currency",
                    currency: "PLN",
                  }).format(x.price)}
                </Box>
              </Row>
              <Row
                className="quantity"
                style={{ display: "flex", paddingLeft: "100px" }}
              >
                <Button onClick={() => dispatch(quantityUp(x))}>
                  {" "}
                  <AddCircleOutlineIcon fontSize="small" />{" "}
                </Button>
                <Typography variant="h6" style={{ padding: "0 10px 0 10px" }}>
                  {x.quantity}
                </Typography>
                <Button onClick={() => dispatch(quantityDown(x))}>
                  {" "}
                  <RemoveCircleOutlineIcon fontSize="small" />{" "}
                </Button>
                <Typography variant="h6" style={{ paddingLeft: "50px" }}>
                  {new Intl.NumberFormat("pl-PL", {
                    style: "currency",
                    currency: "PLN",
                  }).format(x.price * x.quantity)}
                </Typography>
                <Button onClick={() => dispatch(deleteFromCart(x.cart_id))}>
                  {" "}
                  <DeleteIcon />{" "}
                </Button>
              </Row>
            </ListItem>
          ))}
        </List>
      </Paper>
      <Divider style={{ width: "80%", display: "flex", alignSelf: "center" }} />
      <Row className="paymentSummary">
        <Box fontSize="20px" style={{ alignSelf: "flex-end" }}>
          Do zapłaty: {post.shop.cartSum}
        </Box>
        <Row className="shippingInfo">
          <AccountInfo account={post.shop.account} user={post.shop.user} />
          <ShippingInfo shipping={post.shop.shipping} user={post.shop.user} />
        </Row>
        <Row style={{ alignSelf: "flex-end", paddingBottom: "3%" }}>
          <AcceptButton
            account={post.shop.account}
            shipping={post.shop.shipping}
          />
        </Row>
      </Row>
    </Container>
  );
};
