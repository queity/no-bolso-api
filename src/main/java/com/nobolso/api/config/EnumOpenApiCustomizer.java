package com.nobolso.api.config;

import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.ICodigoEnum;
import com.nobolso.api.model.enums.TipoTransacao;
import com.nobolso.api.util.EnumDescricao;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnumOpenApiCustomizer implements OpenApiCustomizer {

    private record EnumSchema<E extends Enum<E> & ICodigoEnum>(String nome, Class<E> enumClass) {}

    private final List<EnumSchema<?>> enums = List.of(
            new EnumSchema<>("TipoTransacao", TipoTransacao.class),
            new EnumSchema<>("DirecaoTransacao", DirecaoTransacao.class),
            new EnumSchema<>("CategoriaTransacao", CategoriaTransacao.class)
    );

    @Override
    public void customise(OpenAPI openApi) {
        if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) return;

        enums.forEach(entry -> {
            Schema<?> schema = openApi.getComponents().getSchemas().get(entry.nome());
            if (schema != null) {
                schema.setDescription(EnumDescricao.of(entry.enumClass()));
            }
        });
    }
}
