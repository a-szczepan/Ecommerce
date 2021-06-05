import "./App.css";
import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { LandingPage } from "./components/LandingPage/LandingPage";
import { Cart } from "./components/Cart";
import { Account } from "./components/Account/Account";
import { Wishlist } from "./components/Wishlist/Wishlist";

function App() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" exact component={LandingPage} />
        <Route path="/account" exact component={Account} />
        <Route path="/cart" exact component={Cart} />
        <Route path="/wishlist" exact component={Wishlist} />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
