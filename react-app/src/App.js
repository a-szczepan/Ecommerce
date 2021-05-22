import './App.css';
import axios from 'axios';
import {Product} from './components/Product'

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
        <Product></Product>
      </header>
    </div>
  );
}

export default App;
