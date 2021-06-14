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
  (providerKey, firstName, lastName) => async (dispatch) => {
    try {
      const res = await axios.post(`http://localhost:9000/accounts`, {
        id: 0,
        providerKey: providerKey,
        firstName: firstName,
        lastName: lastName,
      });
      dispatch({
        type: actionTypes.CREATE_ACCOUNT_INFO,
        payload: res.data,
      });
    } catch (err) {
      console.log(err);
    }
  };

export const deleteAccountInfo = (accountId) => async (dispatch) => {
  try {
    await axios.delete(`http://localhost:9000/accounts/${accountId}`);
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
  (providerKey, streetName, buildingNumber, postalCode, city) =>
  async (dispatch) => {
    try {
      const res = await axios.post(`http://localhost:9000/shipping`, {
        id: 0,
        providerKey: providerKey,
        streetName: streetName,
        buildingNumber: buildingNumber,
        postalCode: postalCode,
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

export const deleteShippingInfo = (shippingId) => async (dispatch) => {
  try {
    await axios.delete(`http://localhost:9000/shipping/${shippingId}`);
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

export const addToCart = (providerKey, productId) => async (dispatch) => {
  try {
    const res = await axios.post(`http://localhost:9000/cart`, {
      id: 0,
      providerKey: providerKey,
      productId: productId,
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

export const deleteFromCart = (cartId) => async (dispatch) => {
  try {
    await axios.delete(`http://localhost:9000/cart/${cartId}`);
    dispatch({
      type: actionTypes.REMOVE_FROM_CART,
      payload: cartId,
    });
  } catch (err) {
    console.log(err);
  }
};

export const quantityUp = (cart_state) => async (dispatch) => {
  try {
    const newQuantity = cart_state.quantity + 1;
    await axios.put(`http://localhost:9000/cart/${cart_state.cartId}`, {
      id: cart_state.cartId,
      providerKey: cart_state.providerKey,
      productId: cart_state.id,
      quantity: newQuantity,
    });
    dispatch({
      type: actionTypes.CART_QUANTITY_UP,
      payload: cart_state.cartId,
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
    await axios.put(`http://localhost:9000/cart/${cart_state.cartId}`, {
      id: cart_state.cartId,
      providerKey: cart_state.providerKey,
      productId: cart_state.id,
      quantity: newQuantity,
    });
    dispatch({
      type: actionTypes.CART_QUANTITY_DOWN,
      payload: cart_state.cartId,
    });
  } catch (err) {
    console.log(err);
  }
};

export const loadWishlistProducts = (wishlist, products) => {
  return (dispatch, getState) => {
    const productsId = wishlist.map((x) => x.productId);
    const data = products.filter((p) => productsId.includes(p.id));
    dispatch({
      type: actionTypes.LOAD_WISHLIST_PRODUCTS,
      payload: data,
    });
  };
};

export const addToWishlist = (providerKey, productId) => async (dispatch) => {
  try {
    const res = await axios.post(`http://localhost:9000/wishlist/create-new`, {
      id: 0,
      providerKey: providerKey,
      productId: productId,
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
      payload: wishlist_id[0].productId,
    });
  } catch (err) {
    console.log(err);
  }
};

export const fetchPayments = (orderId) => {
  console.log("tuu");
  return async (dispatch) => {
    try {
      const res = await axios.get(
        `http://localhost:9000/payments/order/${orderId}`
      );
      console.log(res);
      dispatch({
        type: actionTypes.LOAD_PAYMENTS,
        payload: res.data,
      });
    } catch (err) {}
  };
};

export const fetchOrders = (cartId) => {
  return async (dispatch) => {
    try {
      const res = await axios.get(
        `http://localhost:9000/orders/cart/${cartId}`
      );
      dispatch({
        type: actionTypes.LOAD_ORDERS,
        payload: res.data,
      });
    } catch (err) {}
  };
};

export const createOrder = (cartId, shippingId) => async (dispatch) => {
  try {
    const res = await axios.post(`http://localhost:9000/orders`, {
      id: 0,
      cartId: cartId,
      shippingId: shippingId,
    });

    dispatch({
      type: actionTypes.CREATE_ORDER,
      payload: res.data,
    });
  } catch (err) {}
};

export const createPayment = (orderId, amount) => async (dispatch) => {
  console.log(amount);
  try {
    const date = new Date().toLocaleString();

    const res = await axios.post(`http://localhost:9000/payments`, {
      id: 0,
      orderId: orderId,
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
