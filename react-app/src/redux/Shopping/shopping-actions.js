import * as actionTypes from "./shopping-types";
import axios from "axios";
import Cookies from "js-cookie";

export const setUser = () => {
  return async (dispatch) => {
    const email = Cookies.get("providerKey");
    dispatch({
      type: actionTypes.SET_USER,
      payload: email,
    });
  };
};

export const logOut = () => {
  return async (dispatch) => {
    document.cookie.split(";").forEach(function (c) {
      document.cookie = c
        .replace(/^ +/, "")
        .replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/");
    });
    dispatch({
      type: actionTypes.LOG_OUT,
      payload: "",
    });
  };
};

export const getAccountInfo = (providerKey) => async (dispatch) => {
  try {
    const res = await axios.get(
      `http://localhost:9000/accounts/user/${providerKey}`
    );
    dispatch({
      type: actionTypes.LOAD_ACCOUNT_INFO,
      payload: res.data,
    });
  } catch (err) {
    if (err.response.status === 404) {
      dispatch({
        type: actionTypes.LOAD_ACCOUNT_INFO,
        payload: "create",
      });
    }
  }
};

export const createAccountInfo =
  (providerKey, first_name, last_name) => async (dispatch) => {
    try {
      const res = await axios.post(`http://localhost:9000/accounts`, {
        id: 0,
        providerKey: providerKey,
        first_name: first_name,
        last_name: last_name,
      });
      dispatch({
        type: actionTypes.CREATE_ACCOUNT_INFO,
        payload: res.data,
      });
    } catch (err) {
      console.log(err);
    }
  };

export const deleteAccountInfo = (account_id) => async (dispatch) => {
  try {
    await axios.delete(`http://localhost:9000/accounts/${account_id}`);
    dispatch({
      type: actionTypes.DELETE_ACCOUNT_INFO,
      payload: "create",
    });
  } catch (err) {
    console.log(err);
  }
};

export const getShippingInfo = (providerKey) => async (dispatch) => {
  try {
    const res = await axios.get(
      `http://localhost:9000/shipping/user/${providerKey}`
    );
    dispatch({
      type: actionTypes.LOAD_SHIPMENT_INFO,
      payload: res.data,
    });
  } catch (err) {
    if (err.response.status === 404) {
      dispatch({
        type: actionTypes.LOAD_SHIPMENT_INFO,
        payload: "create",
      });
    }
  }
};

export const createShippingInfo =
  (providerKey, street_name, building_number, postal_code, city) =>
  async (dispatch) => {
    try {
      const res = await axios.post(`http://localhost:9000/shipping`, {
        id: 0,
        providerKey: providerKey,
        street_name: street_name,
        building_number: building_number,
        postal_code: postal_code,
        city: city,
      });
      dispatch({
        type: actionTypes.CREATE_SHIPMENT_INFO,
        payload: res.data,
      });
    } catch (err) {
      console.log(err);
    }
  };

export const deleteShippingInfo = (shipping_id) => async (dispatch) => {
  try {
    await axios.delete(`http://localhost:9000/shipping/${shipping_id}`);
    dispatch({
      type: actionTypes.DELETE_SHIPMENT_INFO,
      payload: "create",
    });
  } catch (err) {
    console.log(err);
  }
};

export const fetchProducts = () => {
  return async (dispatch, getState) => {
    try {
      const res = await axios.get(`http://localhost:9000/products`);
      dispatch({
        type: actionTypes.LOAD_PRODUCTS,
        payload: res.data,
      });
    } catch (err) {}
  };
};

export const fetchWishlist = (providerKey) => {
  return async (dispatch, getState) => {
    try {
      const res = await axios.get(
        `http://localhost:9000/wishlist/user/${providerKey}`
      );
      dispatch({
        type: actionTypes.LOAD_WISHLIST,
        payload: res.data,
      });
    } catch (err) {}
  };
};

export const fetchCart = (providerKey) => {
  return async (dispatch) => {
    try {
      const res = await axios.get(
        `http://localhost:9000/cart/user/${providerKey}`
      );
      dispatch({
        type: actionTypes.LOAD_CART,
        payload: res.data,
      });
    } catch (err) {}
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
    console.log(res);
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
    await axios.delete(`http://localhost:9000/cart/${cart_id}`);
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
    await axios.put(`http://localhost:9000/cart/${cart_state.cart_id}`, {
      id: cart_state.cart_id,
      providerKey: cart_state.providerKey,
      product_id: cart_state.id,
      quantity: newQuantity,
    });
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
    await axios.put(`http://localhost:9000/cart/${cart_state.cart_id}`, {
      id: cart_state.cart_id,
      providerKey: cart_state.providerKey,
      product_id: cart_state.id,
      quantity: newQuantity,
    });
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
    await axios.delete(`http://localhost:9000/wishlist/${wishlist_id[0].id}`);
    dispatch({
      type: actionTypes.REMOVE_FROM_WISHLIST,
      payload: wishlist_id[0].product_id,
    });
  } catch (err) {
    console.log(err);
  }
};

export const fetchPayments = (order_id) => {
  console.log("tuu");
  return async (dispatch) => {
    try {
      const res = await axios.get(
        `http://localhost:9000/payments/order/${order_id}`
      );
      console.log(res);
      dispatch({
        type: actionTypes.LOAD_PAYMENTS,
        payload: res.data,
      });
    } catch (err) {}
  };
};

export const fetchOrders = (cart_id) => {
  return async (dispatch) => {
    try {
      const res = await axios.get(
        `http://localhost:9000/orders/cart/${cart_id}`
      );
      dispatch({
        type: actionTypes.LOAD_ORDERS,
        payload: res.data,
      });
    } catch (err) {}
  };
};

export const createOrder = (cart_id, shipping_id) => async (dispatch) => {
  try {
    const res = await axios.post(`http://localhost:9000/orders`, {
      id: 0,
      cart_id: cart_id,
      shipping_id: shipping_id,
    });

    dispatch({
      type: actionTypes.CREATE_ORDER,
      payload: res.data,
    });
  } catch (err) {}
};

export const createPayment = (order_id, amount) => async (dispatch) => {
  console.log(amount);
  try {
    const date = new Date().toLocaleString();

    const res = await axios.post(`http://localhost:9000/payments`, {
      id: 0,
      order_id: order_id,
      date: date,
      amount: amount,
    });
    console.log(res);
    dispatch({
      type: actionTypes.CREATE_PAYMENT,
      payload: res.data,
    });
  } catch (err) {}
};

export const fetchCategories = () => {
  return async (dispatch) => {
    try {
      const res = await axios.get(`http://localhost:9000/categories`);

      dispatch({
        type: actionTypes.LOAD_CATEGORIES,
        payload: res.data,
      });
    } catch (err) {}
  };
};

export const setCurrentCategory = (category) => {
  console.log(category);
  return (dispatch) => {
    dispatch({
      type: actionTypes.SET_CURRENT_CATEGORY,
      payload: category,
    });
  };
};
