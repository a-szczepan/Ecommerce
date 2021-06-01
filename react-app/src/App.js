import "./App.css";
import React, { useContext } from "react";
import { BrowserRouter, Link, Route } from "react-router-dom";
import { useLocalObservable, useLocalStore, useObserver } from "mobx-react";
import axios from "axios";
import { Product } from "./components/Product";
import { Products } from "./components/Products";
import { Shipping } from "./components/Shipping";
import { Header } from "./components/Header/Header";
import { Categories } from "./components/Categories";
import { Opinion } from "./components/Opinion";
import { Payment } from "./components/Payment";
import { Cart } from "./components/Cart";

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

  return (
    <StoreProvider>
      <BugsList />
      <div className="App">
        <header className="App-header">
          <Cart />
        </header>
        <BrowserRouter>
          <ul>
            <li>
              <Link to="/">Home</Link>
            </li>
          </ul>
          <BugsForm />
          <Route path="/" component={Header} />
        </BrowserRouter>
      </div>
    </StoreProvider>
  );
}

export default App;
