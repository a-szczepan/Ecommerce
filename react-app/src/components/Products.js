import "../components/styles/Product.css";
import { useEffect } from "react";
import Button from "@material-ui/core/Button";
import { makeStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Typography from "@material-ui/core/Typography";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import { connect, useDispatch, useSelector } from "react-redux";
import {
  addToCart,
  addToWishlist,
  fetchCart,
  fetchCategories,
  fetchProducts,
  fetchWishlist,
} from "../redux/Shopping/shopping-actions";

const useStyles = makeStyles({
  root: {
    maxWidth: 345,
  },
});

function ImgMediaCard(props) {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchWishlist(post.shop.user));
    dispatch(fetchCart(post.shop.user));
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
      </CardActions>
    </Card>
  );
}

const Products = (products, wishlist) => {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchCategories());
  }, []);

  const filteredProducts = (index, el) => {
    if (
      el.category_id === post.shop.currentCategory ||
      post.shop.currentCategory === "all"
    )
      return <ImgMediaCard key={index} product={el} wishlist={wishlist} />;
  };

  return (
    <>
      {post.shop.products.map((el, index) =>
        filteredProducts(index, el, wishlist)
      )}
    </>
  );
};

const mapStateToProps = (state) => {
  return {
    products: state.shop.products,
    wishlist: state.shop.wishlist,
    wishlistProducts: state.shop.wishlistProducts,
    cart: state.shop.cart,
  };
};
export default connect(mapStateToProps)(Products);
