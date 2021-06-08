import "./Wishlist.css";
import React, { useEffect } from "react";
import { connect, useDispatch, useSelector } from "react-redux";
import {
    addToCart,
    deleteFromWishlist,
    fetchProducts,
    fetchWishlist,
    loadWishlistProducts,
} from "../../redux/Shopping/shopping-actions";
import DeleteIcon from "@material-ui/icons/Delete";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardMedia from "@material-ui/core/CardMedia";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import { makeStyles } from "@material-ui/core/styles";
import { Container, Row } from "react-bootstrap";
import { Divider } from "@material-ui/core";
import ShoppingCartIcon from "@material-ui/icons/ShoppingCart";

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
          <DeleteIcon
            onClick={(e) => {
              e.preventDefault();
              const wishlist = post.shop.wishlist.filter(
                (x) => x.product_id === props.product.id
              );
              dispatch(deleteFromWishlist(wishlist));
            }}
          />
        </Button>
        <Button>
          <ShoppingCartIcon onClick={() => dispatch(addToCart(1, props.product.id))} />
        </Button>
      </CardActions>
    </Card>
  );
}

export const Wishlist = () => {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchWishlist(1));
    dispatch(loadWishlistProducts(post.shop.wishlist, post.shop.products));
  }, []);

  return (
    <Container className="wishlist">
      <Row className="wishlistHeader">
        <Typography variant="h4">WISHLIST</Typography>
        <Divider />
      </Row>
      <Row className="wishlistProducts">
        {post.shop.wishlistProducts &&
          post.shop.wishlistProducts.map((product, index) => (
            <ImgMediaCard key={index} product={product} />
          ))}
      </Row>
    </Container>
  );
};

const mapStateToProps = (state) => {
  return {
    products: state.shop.products,
    wishlist: state.shop.wishlist,
    wishlistProducts: state.shop.wishlistProducts,
  };
};

export default connect(mapStateToProps)(Wishlist);
