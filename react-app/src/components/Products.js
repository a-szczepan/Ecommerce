import '../components/Products.css';
import {Row, Col, Container} from 'react-bootstrap';
import React, {useState, useEffect} from 'react';
import axios from 'axios';
import sampleImg from '../images/monstera-adasonii.jpg'

export const Products = () => {
    const url = `http://localhost:9000/products`
    const [products, setProducts] = useState(null)
    let content = null
    useEffect(() => {
        axios.get(url).then(response => {
            console.log(response.data)
            setProducts(response.data)
        })
    }, [url])

    if (products) {
        content =
            <>
                {products.map(product => <>
                    <Row className="productName" >
                        <h1>{product.name}</h1>
                    </Row>
                    <Row className="productInfo" >
                        <img src={sampleImg} width="300" height="300"/>
                    </Row>
                </>)}
            </>
    }

    return (
        <Container className="products">
            {content}
        </Container>
    );

}