import '../components/styles/Cart.css';
import {Row, Col, Container} from 'react-bootstrap';
import React, {useState, useEffect} from 'react';
import axios from 'axios';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';

export const Cart = () => {
    const user_id = 1
    const url = `http://localhost:9000/cart/user/${user_id}`
    const [cart, setCart] = useState(null)
    let content = null
    useEffect(() => {
        axios.get(url).then(response => {
            console.log(response.data)
            setCart(response.data)
        })
    }, [url])


    if (cart) {
        content =
            <Row>
                {cart.map(product => <>
                    <Row className="productName" >
                        <h1>{product.product_id}</h1>
                    </Row>
                </>)}
            </Row>
    }

    return (
        <Container className="cart">
            {content}
        </Container>
    );
}