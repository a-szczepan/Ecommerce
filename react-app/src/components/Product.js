import '../components/Product.css';
import {Row, Col, Container} from 'react-bootstrap';
import React, {useState, useEffect} from 'react';
import axios from 'axios';
import sampleImg from '../images/monstera-adasonii.jpg'

export const Product = () => {
    const product_id = 1
    const url = `http://localhost:9000/products/${product_id}`
    const [product, setProduct] = useState(null)
    let content = null
    useEffect(() => {
        axios.get(url).then(response => {
            console.log(response.data)
            setProduct(response.data)
        })
    }, [url])

    if (product) {
        content =
            <>
            <Row className="productName">
                <h1>{product.name}</h1>
            </Row>
        <Row className="productInfo">
            <img src={sampleImg} width="300" height="300"/>
        </Row>
            </>

    }

    return (
        <Container className="product">
            {content}
        </Container>
    );

}