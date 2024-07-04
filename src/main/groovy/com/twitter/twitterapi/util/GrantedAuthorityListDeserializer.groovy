package com.twitter.twitterapi.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class GrantedAuthorityListDeserializer extends JsonDeserializer<List<GrantedAuthority>> {

    @Override
    List<GrantedAuthority> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        node.toList().stream()
                .map { it.get("authority") }
                .map { new SimpleGrantedAuthority(it.asText()) }
                .toList()
    }
}