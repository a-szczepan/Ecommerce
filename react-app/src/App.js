import "./App.css";
import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { LandingPage } from "./components/LandingPage/LandingPage";
import { Account } from "./components/Account/Account";
import { Wishlist } from "./components/Wishlist/Wishlist";
import { ThemeProvider } from "@material-ui/styles";
import { CssBaseline } from "@material-ui/core";
import { Order } from "./components/Order/Order";
import theme from "./theme";

function App() {
  return (
    <BrowserRouter>
      <React.StrictMode>
        <ThemeProvider theme={theme}>
          <CssBaseline />
          <Switch>
            <Route path="/" exact component={LandingPage} />
            <Route path="/account" exact component={Account} />
            <Route path="/wishlist" exact component={Wishlist} />
            <Route path="/order" exact component={Order} />
          </Switch>
        </ThemeProvider>
      </React.StrictMode>
    </BrowserRouter>
  );
}

export default App;
