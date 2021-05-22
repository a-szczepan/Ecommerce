import './App.css';
import axios from 'axios';
import {Product} from './components/Product'
import {Products} from './components/Products'
import {Shipping} from './components/Shipping'

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
        <Shipping></Shipping>
      </header>
    </div>
  );
}

export default App;
