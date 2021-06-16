import { useDispatch, useSelector } from "react-redux";
import React, { useEffect } from "react";
import {
  addToCart,
  addToWishlist,
  deleteFromWishlist,
  fetchCart,
  fetchWishlist,
} from "../redux/Shopping/shopping-actions";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardMedia from "@material-ui/core/CardMedia";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import DeleteIcon from "@material-ui/icons/Delete";
import ShoppingCartIcon from "@material-ui/icons/ShoppingCart";
import { makeStyles } from "@material-ui/core/styles";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";

const useStyles = makeStyles({
  root: {
    maxWidth: 345,
  },
});

const CustomButton = (props) => {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchWishlist(post.shop.user));
    dispatch(fetchCart(post.shop.user));
  }, []);

  if (props.type === "product") {
    return (
      <>
        <Button size="small" style={{ color: "#212529" }}>
          <FavoriteBorderIcon
            onClick={(e) => {
              e.preventDefault();
              dispatch(addToWishlist(post.shop.user, props.product.id));
            }}
          />
        </Button>
        <Button
          size="small"
          style={{ color: "#212529" }}
          onClick={() => {
            dispatch(addToCart(post.shop.user, props.product.id));
          }}
        >
          Do koszyka
        </Button>
      </>
    );
  }
  if (props.type === "wishlist") {
    return (
      <>
        <Button size="small" style={{ color: "#212529" }}>
          <DeleteIcon
            onClick={(e) => {
              e.preventDefault();
              const wishlist = post.shop.wishlist.filter(
                (x) => x.productId === props.product.id
              );
              dispatch(deleteFromWishlist(wishlist));
            }}
          />
        </Button>
        <Button>
          <ShoppingCartIcon
            onClick={() =>
              dispatch(addToCart(post.shop.user, props.product.id))
            }
          />
        </Button>
      </>
    );
  }
};

export const ProductCard = (props) => {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchWishlist(post.shop.user));
  }, []);

  const classes = useStyles();
  return (
    <Card className={classes.root}>
      <CardActionArea>
        <CardMedia
          component="img"
          alt="product"
          height="320"
          image={props.product.image}
          title={props.product.name}
        />
        <CardContent
          style={{
            overflow: "hidden",
            textOverflow: "ellipsis",
            width: "19rem",
          }}
        >
          <Typography noWrap gutterBottom variant="h5" component="h2">
            {props.product.name}
          </Typography>
          <Typography>
            {new Intl.NumberFormat("pl-PL", {
              style: "currency",
              currency: "PLN",
            }).format(props.product.price)}
          </Typography>
        </CardContent>
      </CardActionArea>
      <CardActions>
        <CustomButton type={props.type} product={props.product} />
      </CardActions>
    </Card>
  );
};
