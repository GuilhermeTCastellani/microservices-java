alter table tb_product add column category varchar(50);
alter table tb_product add column seller_id bigint;
alter table tb_product add column seller_name varchar(255);

-- categoria por faixa de preco (produtos atuais estao em USD)
update tb_product set category = 'Premium'       where price >= 800;
update tb_product set category = 'Intermediario' where price >= 300 and price < 800;
update tb_product set category = 'Entrada'       where price < 300;

-- vendedor padrao para os produtos ja existentes (admin = id 1)
update tb_product set seller_id = 1, seller_name = 'AutoParts Oficial';
