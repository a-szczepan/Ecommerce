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
import { ProductCard } from "./ProductCard";

const Products = (products, wishlist) => {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchCategories());
  }, []);

  const filteredProducts = (index, el) => {
    if (
        el.categoryId === post.shop.currentCategory ||
        post.shop.currentCategory === "all"
    )
      return (
          <ProductCard
              key={index}
              product={el}
              wishlist={wishlist}
              type="product"
          />
      );
  };

  return (
      <>{post.shop.products.map((el, index) => filteredProducts(index, el))}</>
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