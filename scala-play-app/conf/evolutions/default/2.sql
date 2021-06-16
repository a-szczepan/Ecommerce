# --- !Ups
INSERT INTO category(name) VALUES
('Monstera'),('Fikus'), ('Filodendron'), ('Begonia'), ('Sansewieria'), ('Alokazja');

INSERT INTO product (categoryId, name, description, image, price)
VALUES (1, 'Monstera Adansonii', '',
        'https://plnts.com/wp-content/uploads/2020/03/PL_M_014_Monstera-Adansonii_potCR_M_1027719-600x750.jpg',
        '35.00'),
       (1, 'Monstera Deliciosa', '',
        'https://plnts.com/wp-content/uploads/2020/03/PL_L_006_Monstera_potCR_L_1051614-600x750.jpg', '89.99'),
       (1, 'Monstera Deliciosa Variegated', '',
        'https://plnts.com/wp-content/uploads/2020/04/PL.EXCL_.31-tumbnail-600x750.jpg', '855.99'),
       (2, 'Fikus Lyrata', '', 'https://plnts.com/wp-content/uploads/2020/03/PL_XL_006_Ficus-Lyrata_potCR_XL_144259-s-1-600x750.jpg','99.99'),
       (2, 'Fikus Tineke', '', 'https://plnts.com/wp-content/uploads/2021/04/PL.XL_.018-tumbnail.jpg', '70.00'),
       (2, 'Fikus Robusta', '', 'https://plnts.com/wp-content/uploads/2021/04/PL.XL_.019-tumbnail-1.jpg', '120.00'),
       (3, 'Filodendron Melanochrysum', '', 'https://plnts.com/wp-content/uploads/2020/08/PL_EXCL_08_Philodendron-Melanochrysum_potCR_M_242207-1.jpg', '299.00'),
       (3, 'Filodendron Black Cardinal', '', 'https://plnts.com/wp-content/uploads/2020/04/PL_M_029_Philodendron-Black-Cardinal_potCR_M_242207-600x750.jpg', '312.99'),
       (4, 'Begonia Masoniana', '', 'https://plnts.com/wp-content/uploads/2020/03/PL_M_002_Begonia-Masoiana_potCR_M_1035836-s-1-600x750.jpg', '45.99'),
       (4, 'Begonia Maculata', '', 'https://plnts.com/wp-content/uploads/2020/03/PL_M_016_Monstera-Begonia-Maculata_potCR_M_1035827-s-1-600x750.jpg', '62.99'),
       (5, 'Sansewieria Superba' , '', 'https://plnts.com/wp-content/uploads/2020/03/PL_L_005_Sanseveria_potCR_L_1027728-s-1-600x750.jpg', '99.99'),
       (6, 'Alokazja Black Zebrina', '', 'https://plnts.com/wp-content/uploads/2020/10/PL_EXCL_19_Alocasia-Black-Zebrina_potCR_L_1027722.jpg', '300.00'),
       (6, 'Alokazja Regal Shields', '', 'https://plnts.com/wp-content/uploads/2020/11/PL_L_014_Alocasia-Regal-Shield_potCR_L_1027722-1.jpg', '215.00');


# --- !Downs