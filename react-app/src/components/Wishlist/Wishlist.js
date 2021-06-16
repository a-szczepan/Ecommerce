import "./Wishlist.css";
import React, { useEffect } from "react";
import { connect, useDispatch, useSelector } from "react-redux";
import {
    fetchProducts,
    fetchWishlist,
    loadWishlistProducts,
} from "../../redux/Shopping/shopping-actions";
import Typography from "@material-ui/core/Typography";
import { Container, Row } from "react-bootstrap";
import { Divider } from "@material-ui/core";
import { ProductCard } from "../ProductCard";

export const Wishlist = () => {
    const dispatch = useDispatch();
    const post = useSelector((state) => state);

    useEffect(() => {
        dispatch(fetchProducts());
        dispatch(fetchWishlist(post.shop.user));
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
                    <ProductCard key={index} product={product} type="wishlist" />
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