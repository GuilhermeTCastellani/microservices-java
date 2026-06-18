create table tb_review (
    id serial not null,
    product_id bigint not null,
    user_id bigint not null,
    user_name varchar(255),
    rating integer not null check (rating between 1 and 5),
    comment varchar(500),
    created_at timestamp not null,
    primary key (id),
    constraint uk_review_product_user unique (product_id, user_id)
);
