import './App.css';
import axios from 'axios';
import {Product} from './components/Product'
import {Products} from './components/Products'
import {Shipping} from './components/Shipping'
import {LandingPage} from './components/LandingPage'
import {Categories} from './components/Categories'

const renderPosts = async() => {
  try {
    const res = await axios.get('http://localhost:9000/categories');
    const posts = res.data;
    return res.data;
  } catch (err) {
    console.log(err);
  }
}

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Categories></Categories>
      </header>
    </div>
  );
}

export default App;
