import './App.css';
import axios from 'axios';
import {Product} from './components/Product'
import {Products} from './components/Products'
import {Shipping} from './components/Shipping'
import {LandingPage} from './components/LandingPage'
import {Categories} from './components/Categories'
import {Opinion} from './components/Opinion'
import {Payment} from './components/Payment'
import {Cart} from './components/Cart'


function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Cart></Cart>
      </header>
    </div>
  );
}

export default App;
