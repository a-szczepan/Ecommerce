import "./App.css";
import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { LandingPage } from "./components/LandingPage/LandingPage";
import { Cart } from "./components/Cart/Cart";
import { Account } from "./components/Account/Account";
import { Wishlist } from "./components/Wishlist/Wishlist";
import { ThemeProvider } from "@material-ui/styles";
import { CssBaseline } from "@material-ui/core";
import theme from "./theme";

function App() {
  return (
    <BrowserRouter>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Switch>
          <Route path="/" exact component={LandingPage} />
          <Route path="/account" exact component={Account} />
          {/*<Route path="/cart" exact component={Cart} />*/}
          <Route path="/wishlist" exact component={Wishlist} />
        </Switch>
      </ThemeProvider>
    </BrowserRouter>
  );
}

export default App;
