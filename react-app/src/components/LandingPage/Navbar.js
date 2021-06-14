import { useHistory } from "react-router-dom";
import React, { useEffect } from "react";
import { Col } from "react-bootstrap";
import Button from "@material-ui/core/Button";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchCategories,
  fetchOrders,
  fetchProducts,
  fetchWishlist,
  loadWishlistProducts,
  logOut,
  setUser,
} from "../../redux/Shopping/shopping-actions";
import { CartDialog } from "../Cart/Cart";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import {
  GithubLoginButton,
  GoogleLoginButton,
} from "react-social-login-buttons";
import { withStyles } from "@material-ui/core/styles";

function AccountButton() {
  const dispatch = useDispatch();
  const post = useSelector((state) => state);
  const history = useHistory();

  const StyledAccountMenu = withStyles({
    paper: {
      backgroundColor: "#dee7de",
    },
  })((props) => (
    <Menu
      elevation={-1}
      getContentAnchorEl={null}
      anchorOrigin={{
        vertical: "bottom",
        horizontal: "center",
      }}
      transformOrigin={{
        vertical: "top",
        horizontal: "center",
      }}
      {...props}
    />
  ));

  const [anchorElement, setAnchorElement] = React.useState(null);

  const handleClick = (event) => {
    setAnchorElement(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorElement(null);
  };

  return (
    <>
      <Button
        aria-controls="customized-menu"
        aria-haspopup="true"
        onClick={handleClick}
      >
        Konto
      </Button>
      <StyledAccountMenu
        id="customized-menu"
        anchorEl={anchorElement}
        keepMounted
        open={Boolean(anchorElement)}
        onClose={handleClose}
      >
        <MenuItem>
          <Button onClick={() => history.push("/settings")}>Ustawienia</Button>
        </MenuItem>
        <MenuItem>
          <Button
            onClick={() => {
              history.push("/orders");
              dispatch(fetchOrders(post.shop.cart[0].cart_id));
            }}
          >
            Zamówienia
          </Button>
        </MenuItem>
        <MenuItem>
          <Button
            onClick={() => {
              dispatch(logOut());
            }}
          >
            Wyloguj
          </Button>
        </MenuItem>
      </StyledAccountMenu>
    </>
  );
}

const SignIn = () => {
  const StyledMenu = withStyles({
    paper: {
      backgroundColor: "#27401A",
      backgroundImage: "linear-gradient(160deg, #8BB06C 0%, #27401A 100%)",
    },
  })((props) => (
    <Menu
      elevation={-1}
      getContentAnchorEl={null}
      anchorOrigin={{
        vertical: "bottom",
        horizontal: "center",
      }}
      transformOrigin={{
        vertical: "top",
        horizontal: "center",
      }}
      {...props}
    />
  ));

  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <>
      <Button
        aria-controls="customized-menu"
        aria-haspopup="true"
        onClick={handleClick}
      >
        Zaloguj
      </Button>
      <StyledMenu
        id="customized-menu"
        anchorEl={anchorEl}
        keepMounted
        open={Boolean(anchorEl)}
        onClose={handleClose}
      >
        <MenuItem onClick={handleClose}>
          <GoogleLoginButton
            style={{ height: "35px", width: "250px" }}
            onClick={() => {
              window.location.assign(
                "http://localhost:9000" + "/authenticate/google"
              );
            }}
          />
        </MenuItem>
        <MenuItem onClick={handleClose}>
          <GithubLoginButton
            style={{ height: "35px", width: "250px" }}
            onClick={() =>
              window.location.assign(
                "http://localhost:9000" + "/authenticate/github"
              )
            }
          />
        </MenuItem>
      </StyledMenu>
    </>
  );
};

function Account() {
  const post = useSelector((state) => state);
  if (post.shop.user) {
    return (
      <>
        <WishlistButton />
        <CartDialog />
        <AccountButton />
      </>
    );
  }
  return <SignIn />;
}

function WishlistButton() {
  const history = useHistory();
  const dispatch = useDispatch();

  const post = useSelector((state) => state);

  useEffect(() => {
    dispatch(fetchProducts());
    dispatch(fetchWishlist(post.shop.user));
  }, []);

  function handleClick() {
    dispatch(loadWishlistProducts(post.shop.wishlist, post.shop.products));
    history.push("/wishlist");
  }

  return (
    <Button type="button" onClick={handleClick}>
      Wishlist
    </Button>
  );
}

export const Navbar = () => {
  return (
    <Col className="navbar">
      <Account />
    </Col>
  );
};
