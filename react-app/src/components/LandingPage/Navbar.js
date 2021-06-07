import { useHistory } from "react-router-dom";
import React, { useEffect } from "react";
import { Col, Row } from "react-bootstrap";
import Button from "@material-ui/core/Button";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchProducts,
  fetchWishlist,
  loadWishlistProducts,
} from "../../redux/Shopping/shopping-actions";

function AccountButton() {
  const history = useHistory();
  function handleClick() {
    history.push("/account");
  }
  return (
    <Button type="button" onClick={handleClick}>
      Konto
    </Button>
  );
}

function WishlistButton() {
  const history = useHistory();
  const dispatch = useDispatch();

  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchWishlist(1));
  }, []);

  function handleClick() {
    dispatch(loadWishlistProducts(post.shop.wishlist, post.shop.products));
    history.push("/wishlist");
  }
  return (
    <Button type="button" onClick={handleClick}>
      Wishlist
    </Button>
  );
}

export const Navbar = () => {
  return (
    <Col className="navbar">
      <Row>
        <WishlistButton />
        <Button>Koszyk</Button>
        <AccountButton />
      </Row>
    </Col>
  );
};
