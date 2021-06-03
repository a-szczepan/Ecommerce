import "./App.css";
import React, { useContext } from "react";
import { BrowserRouter, Link, Route, Switch } from "react-router-dom";
import { useLocalObservable, useLocalStore, useObserver } from "mobx-react";
import axios from "axios";

import { LandingPage } from "./components/LandingPage/LandingPage";
import { Categories } from "./components/Categories";
import { Opinion } from "./components/Opinion";
import { Payment } from "./components/Payment";
import { Cart } from "./components/Cart";
import { Account } from "./components/Account/Account";
import {Wishlist} from "./components/Wishlist/Wishlist";

const StoreContext = React.createContext();

const StoreProvider = ({ children }) => {
  const store = useLocalObservable(() => ({
    bugs: ["Centipede"],
    addBug: (bug) => {
      store.bugs.push(bug);
    },
  }));
  return (
    <StoreContext.Provider value={store}>{children}</StoreContext.Provider>
  );
};

const BugsList = () => {
  const store = React.useContext(StoreContext);
  return useObserver(() => (
    <ul>
      {store.bugs.map((bug) => (
        <li key={+Math.random().toFixed()}>{bug}</li>
      ))}
    </ul>
  ));
};

const BugsForm = () => {
  const store = React.useContext(StoreContext);
  const [bug, setBug] = React.useState("");
  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        store.addBug(bug);
        setBug("");
      }}
    >
      <input type="text" value={bug} onChange={(e) => setBug(e.target.value)} />
      <button type="submit">Add</button>
    </form>
  );
};

function App() {
  const store = useContext(StoreContext);

  /*return (
    <StoreProvider>
      <BrowserRouter>
          <Switch>
              <Route path="/" component={Header} />
              <Route path="/account" component={Account} />
          </Switch>
      </BrowserRouter>
      <BugsList />
      <div className="App">
        <header className="App-header">
          <Cart />
        </header>
        <BugsForm />
      </div>
    </StoreProvider>
  );*/
  return (
    <StoreProvider>
      <BrowserRouter>
        <Switch>
          <Route path="/" exact component={LandingPage} />
          <Route path="/account" exact component={Account} />
          <Route path="/cart" exact component={Cart} />
          <Route path="/wishlist" exact component={Wishlist}/>
        </Switch>
      </BrowserRouter>
    </StoreProvider>
  );
}

export default App;
