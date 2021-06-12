import * as actionTypes from "./shopping-types";
import Axios from "axios";
import axios from "axios";
import Cookies from "js-cookie";

export const setUser = () => {
  return async (dispatch) => {
    const email = Cookies.get('providerKey')
    dispatch({
      type: actionTypes.SET_USER,
      payload: email
    })
  }
}

export const fetchProducts = () => {
  return async (dispatch, getState) => {
    const res = await Axios.get(`http://localhost:9000/products`);
    dispatch({
      type: actionTypes.LOAD_PRODUCTS,
      payload: res.data,
    });
  };
};

export const fetchWishlist = (providerKey) => {
  return async (dispatch, getState) => {
    const res = await Axios.get(
      `http://localhost:9000/wishlist/user/${providerKey}`
    );
    dispatch({
      type: actionTypes.LOAD_WISHLIST,
      payload: res.data,
    });
  };
};

export const fetchCart = (providerKey) => {
  return async (dispatch) => {
    const res = await axios.get(`http://localhost:9000/cart/user/${providerKey}`);
    dispatch({
      type: actionTypes.LOAD_CART,
      payload: res.data,
    });
  };
};

export const addToCart = (providerKey, product_id) => async (dispatch) => {
  try {
    const res = await axios.post(`http://localhost:9000/cart`, {
      id: 0,
      providerKey: providerKey,
      product_id: product_id,
      quantity: 1,
    });
    dispatch({
      type: actionTypes.ADD_TO_CART,
      payload: res.data,
    });
  } catch (err) {
    console.log(err);
  }
};

export const deleteFromCart = (cart_id) => async (dispatch) => {
  try {
    const res = await Axios.delete(`http://localhost:9000/cart/${cart_id}`);
    dispatch({
      type: actionTypes.REMOVE_FROM_CART,
      payload: cart_id,
    });
  } catch (err) {
    console.log(err);
  }
};

export const quantityUp = (cart_state) => async (dispatch) => {
  try {
    const newQuantity = cart_state.quantity + 1;
    const res = await Axios.put(
      `http://localhost:9000/cart/${cart_state.cart_id}`,
      {
        id: cart_state.cart_id,
        providerKey: cart_state.providerKey,
        product_id: cart_state.id,
        quantity: newQuantity,
      }
    );
    dispatch({
      type: actionTypes.CART_QUANTITY_UP,
      payload: cart_state.cart_id,
    });
  } catch (err) {
    console.log(err);
  }
};

export const quantityDown = (cart_state) => async (dispatch) => {
  try {
    let newQuantity = 0;
    if (cart_state.quantity > 0) {
      newQuantity = cart_state.quantity - 1;
    }
    const res = await Axios.put(
      `http://localhost:9000/cart/${cart_state.cart_id}`,
      {
        id: cart_state.cart_id,
        providerKey: cart_state.providerKey,
        product_id: cart_state.id,
        quantity: newQuantity,
      }
    );
    dispatch({
      type: actionTypes.CART_QUANTITY_DOWN,
      payload: cart_state.cart_id,
    });
  } catch (err) {
    console.log(err);
  }
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

export const addToWishlist = (providerKey, product_id) => async (dispatch) => {
  try {
    const res = await axios.post(`http://localhost:9000/wishlist/create-new`, {
      id: 0,
      providerKey: providerKey,
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
