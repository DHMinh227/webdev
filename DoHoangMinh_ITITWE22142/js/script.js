// Set copyright years
document.querySelectorAll('[id^="year"]').forEach(el => el.textContent = new Date().getFullYear());

// Simple client-side form validation and meter update
(function(){
  const form = document.getElementById('contactForm');
  if(!form) return;
  const message = document.getElementById('message');
  const meter = document.getElementById('progress');
  const status = document.getElementById('formMessage');

  function updateMeter(){
    if(meter && message){
      meter.value = message.value.length;
      meter.textContent = `${message.value.length}/2000`;
    }
  }

  message && message.addEventListener('input', updateMeter);

  form.addEventListener('submit', function(e){
    e.preventDefault();
    // Built-in validation first
    if(!form.checkValidity()){
      status.textContent = 'Please complete required fields and fix errors.';
      status.style.color = 'crimson';
      form.reportValidity();
      return;
    }

    // Example pattern check (phone)
    const tel = form.querySelector('[name="tel"]');
    if(tel && tel.value && !/^\+?\d{7,15}$/.test(tel.value)){
      status.textContent = 'Please enter a valid phone number.';
      status.style.color = 'crimson';
      tel.focus();
      return;
    }

    // Simulate successful send
    status.textContent = 'Thank you! Your message was sent (demo).';
    status.style.color = 'green';
    form.reset();
    updateMeter();
  }, false);
})();
