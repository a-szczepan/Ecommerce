import "./Account.css";
import { Container } from "@material-ui/core";
import {useSelector} from "react-redux";
export const Account = () => {
  const post = useSelector((state) => state);
  return <Container className="accountComponent">{post.shop.user}</Container>;
};
