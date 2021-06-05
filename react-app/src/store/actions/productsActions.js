import { PRODUCTS_ERROR, GET_PRODUCTS } from "../types";
import axios from "axios";

export const getProducts = () => async (dispatch) => {
  try {
    const res = await axios.get(`http://localhost:9000/products`);
    dispatch({
      type: GET_PRODUCTS,
      payload: res.data,
    });
  } catch (e) {
    dispatch({
      type: PRODUCTS_ERROR,
      payload: console.log(e),
    });
  }
};
