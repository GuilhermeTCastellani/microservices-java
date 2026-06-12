alter table tb_product add column image_url varchar(255);

update tb_product set image_url = '';

update tb_product set image_url = 'https://placehold.co/600x400?text=iPhone+15' where description = 'iPhone 15 128GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=iPhone+15+Pro' where description = 'iPhone 15 Pro 256GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Galaxy+S24' where description = 'Galaxy S24 256GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Galaxy+S24+Ultra' where description = 'Galaxy S24 Ultra 512GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Moto+G84' where description = 'Moto G84 256GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Moto+Edge+40' where description = 'Moto Edge 40 256GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Redmi+Note+13+Pro' where description = 'Redmi Note 13 Pro 256GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Redmi+13C' where description = 'Redmi 13C 128GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Pixel+8' where description = 'Pixel 8 128GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Pixel+8+Pro' where description = 'Pixel 8 Pro 256GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=OnePlus+12' where description = 'OnePlus 12 256GB';
update tb_product set image_url = 'https://placehold.co/600x400?text=Galaxy+A55' where description = 'Galaxy A55 128GB';
