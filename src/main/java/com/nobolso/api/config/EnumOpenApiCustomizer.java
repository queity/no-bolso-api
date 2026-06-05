package com.nobolso.api.config;

import com.nobolso.api.model.enums.ICodigoEnum;
import com.nobolso.api.util.EnumDescricao;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class EnumOpenApiCustomizer implements OpenApiCustomizer, ApplicationContextAware {

    private ApplicationContext context;
    private Map<String, String> descricoesPorParam;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) {
        this.context = context;
    }

    private Map<String, String> getDescricoes() {
        if (descricoesPorParam != null) return descricoesPorParam;

        descricoesPorParam = new HashMap<>();

        context.getBeansWithAnnotation(RestController.class).values().forEach(bean ->
                Arrays.stream(bean.getClass().getDeclaredMethods()).forEach(method ->
                        Arrays.stream(method.getParameters()).forEach(param -> {
                            Schema schema = getSchemaFromParameter(param);
                            if (schema == null || !ICodigoEnum.class.isAssignableFrom(schema.implementation())) return;

                            String paramName = resolveParamName(param);
                            if (paramName == null) return;

                            @SuppressWarnings("unchecked")
                            Class<? extends ICodigoEnum> enumClass =
                                    (Class<? extends ICodigoEnum>) schema.implementation();

                            descricoesPorParam.put(paramName, EnumDescricao.of(enumClass));
                        })
                )
        );

        return descricoesPorParam;
    }

    private Schema getSchemaFromParameter(java.lang.reflect.Parameter param) {
        Parameter paramAnnotation = param.getAnnotation(Parameter.class);
        if (paramAnnotation != null && paramAnnotation.schema().implementation() != Void.class) {
            return paramAnnotation.schema();
        }
        Schema schemaAnnotation = param.getAnnotation(Schema.class);
        if (schemaAnnotation != null && schemaAnnotation.implementation() != Void.class) {
            return schemaAnnotation;
        }
        return null;
    }

    private String resolveParamName(java.lang.reflect.Parameter param) {
        RequestParam requestParam = param.getAnnotation(RequestParam.class);
        if (requestParam == null) return null;
        return requestParam.value().isEmpty() ? param.getName() : requestParam.value();
    }

    @Override
    public void customise(OpenAPI openApi) {
        if (openApi.getPaths() == null) return;

        Map<String, String> descricoes = getDescricoes();

        openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    if (operation.getParameters() == null) return;
                    operation.getParameters().forEach(param -> {
                        String descricao = descricoes.get(param.getName());
                        if (descricao == null) return;

                        String base = param.getDescription() != null ? param.getDescription() + " — " : "";
                        param.setDescription(base + descricao);

                        io.swagger.v3.oas.models.media.Schema<?> schema = param.getSchema();
                        if (schema != null) schema.setDescription(descricao);
                    });
                })
        );
    }
}
