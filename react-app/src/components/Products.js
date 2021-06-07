import "../components/styles/Product.css";
import { useEffect } from "react";
import Button from "@material-ui/core/Button";
import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Typography from "@material-ui/core/Typography";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import { connect } from "react-redux";
import {
  addToWishlist,
  fetchProducts,
  fetchWishlist,
} from "../redux/Shopping/shopping-actions";
import { useDispatch, useSelector } from "react-redux";

const useStyles = makeStyles({
  root: {
    maxWidth: 345,
  },
});

function ImgMediaCard(props) {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchWishlist(1));
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
        <Button size="small" style={{ color: "#212529" }}>
          <FavoriteBorderIcon
            onClick={(e) => {
              e.preventDefault();
              dispatch(addToWishlist(1, props.product.id));
            }}
          />
        </Button>
        <Button size="small" style={{ color: "#212529" }}>
          Do koszyka
        </Button>
      </CardActions>
    </Card>
  );
}

const Products = (products, wishlist) => {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchProducts());
  }, []);

  return (
    <>
      {post.shop.products.map((el, index) => (
        <ImgMediaCard key={index} product={el} wishlist={wishlist}>
          {" "}
        </ImgMediaCard>
      ))}
    </>
  );
};

const mapStateToProps = (state) => {
  //co chce ze state reducera - mamy dostęp do propsów
  return {
    products: state.shop.products,
    wishlist: state.shop.wishlist,
    wishlistProducts: state.shop.wishlistProducts,
  };
};
export default connect(mapStateToProps)(Products);
