import '../components/styles/Shipping.css';
import {Row, Col, Container} from 'react-bootstrap';
import React, {useState, useEffect} from 'react';
import axios from 'axios';
import Box from '@material-ui/core/Box';

export const Shipping = () => {
    const url = `http://localhost:9000/shipping`
    const [shipping, setShipping] = useState(null)
    let content = null
    useEffect(() => {
        axios.get(url).then(response => {
            console.log(response.data)
            setShipping(response.data)
        })
    }, [url])

    if (shipping) {
        content =
            <>  <h1 className="heading">Adres wysyłki:</h1>
                {shipping.map(shipping => <>
                    <Row className="shippingData">
                        <Box fontWeight={800}> Ulica:</Box> <Box >{shipping.street_name}</Box>
                    </Row>
                    <Row className="shippingData" >
                        <Box fontWeight={800}> Numer budynku:</Box> <Box>{shipping.building_number}</Box>
                    </Row>
                    <Row className="shippingData" >
                        <Box fontWeight={800}> Numer mieszkania:</Box> <Box>{shipping.apartment_number}</Box>
                    </Row>
                    <Row className="shippingData" >
                        <Box fontWeight={800}> Miasto:</Box> <Box>{shipping.city}</Box>
                    </Row>
                    <Row className="shippingData">
                        <Box fontWeight={800}> Kod pocztowy:</Box> <Box>{shipping.postal_code}</Box>
                    </Row>
                </>)}
            </>
    }

    return (
        <Container className="shipping">
            {content}
        </Container>
    );
}