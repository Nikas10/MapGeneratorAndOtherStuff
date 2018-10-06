varying vec2 v_texCoords;
uniform sampler2D u_texture;
void main(void) {
    vec2 p = -1.0 + 2.0 * gl_FragCoord.xy / v_texCoords.xy;
    vec2 uv;
    float a = atan(p.y,p.x) / (2.0*3.1416);
    float r = sqrt(dot(p,p))/sqrt(2.0);
    uv.x = r;
    uv.y = a+r;
    vec3 col = texture2D(u_texture,uv).xyz;
    gl_FragColor = vec4(col,1.0);
}