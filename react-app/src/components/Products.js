import "../components/styles/Product.css";
import { useEffect } from "react";
import { connect, useDispatch, useSelector } from "react-redux";
import {
  fetchCategories,
  fetchProducts,
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