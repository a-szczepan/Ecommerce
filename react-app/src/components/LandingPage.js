import '../components/styles/LandingPage.css';
import Box from '@material-ui/core/Box';
import {Row, Col, Container} from 'react-bootstrap';

export const LandingPage = () => {
    return (
        <Row className="landingPage">
            <Box fontSize="h2.fontSize">The Plant Corner</Box>
            <Box fontSize="h6.fontSize">Oferujemy tropikalne rośliny z całego świata. Wybieramy je dla Ciebie osobiście. Dostarczamy prosto pod Twoje drzwi. </Box>
        </Row>
    );
}