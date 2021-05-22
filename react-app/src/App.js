import './App.css';
import axios from 'axios';
import {Product} from './components/Product'
import {Products} from './components/Products'
import {Shipping} from './components/Shipping'
import {LandingPage} from './components/LandingPage'
import {Categories} from './components/Categories'
import {Opinion} from './components/Opinion'


function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Opinion></Opinion>
      </header>
    </div>
  );
}

export default App;
