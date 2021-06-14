import "./Account.css";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import {
  fetchCart,
  setUser,
} from "../../redux/Shopping/shopping-actions";

export const Orders = () => {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(setUser());
    dispatch(fetchCart(post.shop.user));
  }, []);

  return <>{}</>;
};
