{{/*
公共标签定义
*/}}
{{- define "sobre-chart.labels"}}
app.kubernetes.io/name: todo
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
app.kubernetes.io/component: backend
app.kubernetes.io/part-of: sobre
app.kubernetes.io/managed-by: helm
app.kubernetes.io/created-by: controller-manager
{{- include "sobre-chart.selectLabels" . }}
{{- end }}


{{/*
选择标签定义
*/}}
{{- define "sobre-chart.selectLabels"}}
app: sobre
{{- end}}

{{/*
环境属性定义
*/}}
{{- define "sobre-chart.env"}}
- name: appVersion
  value: {{ .Chart.AppVersion }}
{{- end}}

{{/*
服务端口定义
*/}}
{{- define "sobre-chart.ports"}}
ports:
  - port: 8080
    targetPort: 8080
    name: http
{{- end}}

{{/*
secret定义
*/}}
{{- define "sobre-chart.secret"}}
imagePullSecrets:
  - name: dockercfg-sercet
{{- end}}

{{/*
docker配置定义，具体参数值通过helm install -set进行设置
*/}}
{{- define "sobre-chart.dockercfg"}}
{{- with .Values.imageCredentials}}
{{- printf "{\"auths\":{\"%s\":{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"auth\":\"%s\"}}}" .registry .username .password .email (printf "%s:%s" .username .password | b64enc) | b64enc }}
{{- end}}
{{- end}}