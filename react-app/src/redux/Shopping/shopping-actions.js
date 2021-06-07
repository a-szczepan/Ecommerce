import * as actionTypes from "./shopping-types";
import Axios from "axios";
import axios from "axios";

export const fetchProducts = () => {
  return async (dispatch, getState) => {
    const res = await Axios.get(`http://localhost:9000/products`);
    dispatch({
      type: actionTypes.LOAD_PRODUCTS,
      payload: res.data,
    });
  };
};

export const fetchWishlist = (user_id) => {
  return async (dispatch, getState) => {
    const res = await Axios.get(
      `http://localhost:9000/wishlist/user/${user_id}`
    );
    dispatch({
      type: actionTypes.LOAD_WISHLIST,
      payload: res.data,
    });
  };
};

export const loadWishlistProducts = (wishlist, products) => {
  return (dispatch, getState) => {
    const productsId = wishlist.map((x) => x.product_id);
    const data = products.filter((p) => productsId.includes(p.id));
    dispatch({
      type: actionTypes.LOAD_WISHLIST_PRODUCTS,
      payload: data,
    });
  };
};

export const addToWishlist = (user_id, product_id) => async (dispatch) => {
  try {
    const res = await axios.post(`http://localhost:9000/wishlist/create-new`, {
      id: 0,
      user_id: user_id,
      product_id: product_id,
    });
    dispatch({
      type: actionTypes.ADD_TO_WISHLIST,
      payload: res.data,
    });
  } catch (err) {
    console.log(err);
  }
};

export const deleteFromWishlist = (wishlist_id) => async (dispatch) => {
  try {
    const res = await Axios.delete(
      `http://localhost:9000/wishlist/${wishlist_id[0].id}`
    );
    dispatch({
      type: actionTypes.REMOVE_FROM_WISHLIST,
      payload: wishlist_id[0].product_id,
    });
  } catch (err) {
    console.log(err);
  }
};